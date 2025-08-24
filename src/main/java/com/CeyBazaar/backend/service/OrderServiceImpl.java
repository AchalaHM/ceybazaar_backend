package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.*;
import com.CeyBazaar.backend.entity.Order;
import com.CeyBazaar.backend.entity.OrderDetails;
import com.CeyBazaar.backend.entity.OrderItem;
import com.CeyBazaar.backend.repository.OrderDetailsRepository;
import com.CeyBazaar.backend.repository.OrderItemRepository;
import com.CeyBazaar.backend.repository.OrderRepository;
import com.CeyBazaar.backend.repository.ProductRepository;
import com.CeyBazaar.backend.entity.Product;
import com.CeyBazaar.backend.util.Constants;
import com.CeyBazaar.backend.util.PayHereGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService{
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PdfService pdfService;

    @Transactional // Ensures atomicity: all operations succeed or rollback
    @Override
    public Response<PaymentDTO> addNewOrder(OrderDTO orderDTO) {

        String OID;
        do {
            OID = generateRandomID(); // Generate a new order ID
        } while (orderRepository.existsByOrderId(OID)); // Fixed method name

        try {
            // Create Order entity
            Order order = new Order();
            order.setOrderId(OID);
            order.setStatus(Constants.UNPAID);
            order.setDeliveryStatus(Constants.DELIVERY_PENDING);
            order.setTotalCost(orderDTO.getTotalCost());
            order.setShippingCost(orderDTO.getShippingCost() != null ? orderDTO.getShippingCost() : 0.0);
            order.setAddedOn(LocalDate.now());

            // Save order first (important for foreign key relation)
            order = orderRepository.save(order);

            // Create and save Order Items
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order); // Set the parent order
                orderItem.setItemId(itemDTO.getId());
                orderItem.setQuantity(itemDTO.getQty());
                orderItems.add(orderItem);
            }
            orderItemRepository.saveAll(orderItems); // Batch insert order items

            // Create and save Order Details
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order); // Set the parent order
            orderDetails.setCustomerName(orderDTO.getCustomerName());
            orderDetails.setCustomerEmail(orderDTO.getCustomerEmail());
            orderDetails.setAddress(orderDTO.getAddress());
            orderDetails.setRegion(orderDTO.getRegion());
            orderDetails.setCardName(orderDTO.getCardName());
            orderDetails.setCardNumber(orderDTO.getCardNumber());

            orderDetailsRepository.save(orderDetails);
            logger.info("Order placed successfully with order ID : " + OID);

            // Email will be sent after payment confirmation

//            emailService.sendEmail(orderDTO.getCustomerEmail() ,"ceybazaar.business@gmail.com", "Order Confirmation :"+ OID ,htmlBody);
            String hash = PayHereGenerator.generateHash(Constants.MERCHANT_ID , OID , orderDTO.getTotalCost() , "LKR" , Constants.MERCHANT_SECRET);
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setHash(hash);
            paymentDTO.setOid(OID);

            System.out.println(hash);

            return new Response<>(Constants.SUCCESS, "Order placed successfully", paymentDTO);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException("Order creation failed", ex);
            // Triggers rollback
        }

    }

    @Override
    public Response<String> updateOrder(String orderId){
        try {
            Order order = orderRepository.findByOrderId(orderId);
            if (order == null || order.getOrderDetails() == null) {
                logger.error("Order or OrderDetails not found for orderId: " + orderId);
                return new Response<>(Constants.RUNTIME_EXCEPTION, "Order not found", null);
            }
            
            order.setStatus(Constants.PAID);
            order.setDeliveryStatus(Constants.DELIVERY_PROGRESSING);
            order.setUpdatedOn(LocalDate.now());
            orderRepository.save(order);
            logger.info("Payment status updated successfully");
            
            String customerName = order.getOrderDetails().getCustomerName();
            String customerEmail = order.getOrderDetails().getCustomerEmail();
            String htmlBody = generateEmailBody(order);
            
            byte[] pdfBytes = pdfService.generateOrderPdf(order);
            emailService.sendEmailWithAttachment(customerEmail, "Order Confirmation: " + orderId, htmlBody, pdfBytes, "Order_" + orderId + ".pdf");
            return new Response<>(Constants.SUCCESS, "Payment status updated successfully", "SUCCESS");
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION, null, null);
        }
    }

    @Override
    public Response<PaymentStatusDTO> getPaymentStatus(String orderId){
        try{
            Order order = orderRepository.findByOrderId(orderId);
            PaymentStatusDTO paymentStatusDTO = new PaymentStatusDTO();
            paymentStatusDTO.setStatus(order.getStatus());

            logger.info("Payment status is : " + order.getStatus());
            return new Response<>(Constants.SUCCESS ,"Payment status is : " + order.getStatus() , paymentStatusDTO);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION , null , null);
        }
    }

    private String generateEmailBody(Order order) {
        StringBuilder orderItemsHtml = new StringBuilder();
        double totalAmount = 0;
        
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getItemId()).orElse(null);
            if (product != null) {
                double itemTotal = product.getPrice() * item.getQuantity();
                totalAmount += itemTotal;
                
                String productImage = product.getImagePath() != null ? product.getImagePath() : 
                    "https://via.placeholder.com/80x80?text=No+Image";
                
                orderItemsHtml.append(String.format("""
                    <tr>
                        <td style="padding: 15px; border-bottom: 1px solid #eee;">
                            <table width="100%%" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td width="80">
                                        <img src="%s" alt="%s" style="width: 80px; height: 80px; object-fit: cover; border-radius: 5px;">
                                    </td>
                                    <td style="padding-left: 15px; vertical-align: top;">
                                        <h4 style="margin: 0 0 5px 0; font-size: 16px; color: #333;">%s</h4>
                                        <p style="margin: 0; color: #666; font-size: 14px;">Quantity: %d</p>
                                        <p style="margin: 5px 0 0 0; color: #666; font-size: 14px;">Price: LKR %.2f</p>
                                    </td>
                                    <td align="right" style="vertical-align: top;">
                                        <p style="margin: 0; font-weight: bold; font-size: 16px; color: #333;">LKR %.2f</p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    """, productImage, product.getProductName(), product.getProductName(), 
                    item.getQuantity(), product.getPrice(), itemTotal));
            }
        }
        
        return String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1">
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif;">
            <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f4f4f4; padding: 20px;">
                <tr>
                    <td align="center">
                        <table role="presentation" width="100%%" max-width="600px" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                            
                            <!-- Header -->
                            <tr>
                                <td style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 30px 20px; text-align: center;">
                                    <img src="https://ceybazaar.s3.eu-north-1.amazonaws.com/static/media/logo.b0497aa0d64a5ebc05b8.png" alt="CeyBazaar" style="max-width: 120px; height: auto; margin-bottom: 15px;">
                                    <h1 style="color: white; margin: 0; font-size: 24px;">Order Confirmation</h1>
                                    <p style="color: rgba(255,255,255,0.9); margin: 5px 0 0 0; font-size: 16px;">Order #%s</p>
                                </td>
                            </tr>
                            
                            <!-- Greeting -->
                            <tr>
                                <td style="padding: 30px 20px 20px 20px;">
                                    <h2 style="color: #333; margin: 0 0 15px 0; font-size: 20px;">Hi %s!</h2>
                                    <p style="color: #666; margin: 0; font-size: 16px; line-height: 24px;">Thank you for your order! We're excited to get your items ready for delivery.</p>
                                </td>
                            </tr>
                            
                            <!-- Order Items -->
                            <tr>
                                <td style="padding: 0 20px;">
                                    <h3 style="color: #333; margin: 0 0 20px 0; font-size: 18px; border-bottom: 2px solid #667eea; padding-bottom: 10px;">Order Details</h3>
                                    <table width="100%%" cellspacing="0" cellpadding="0" border="0" style="border: 1px solid #eee; border-radius: 5px;">
                                        %s
                                        <tr>
                                            <td style="padding: 20px; background-color: #f8f9fa; border-top: 2px solid #667eea;">
                                                <table width="100%%" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td style="font-size: 16px; color: #333; padding-bottom: 5px;">Subtotal:</td>
                                                        <td align="right" style="font-size: 16px; color: #333; padding-bottom: 5px;">LKR %.2f</td>
                                                    </tr>
                                                    <tr>
                                                        <td style="font-size: 16px; color: #333; padding-bottom: 10px;">Shipping:</td>
                                                        <td align="right" style="font-size: 16px; color: #333; padding-bottom: 10px;">LKR %.2f</td>
                                                    </tr>
                                                    <tr style="border-top: 1px solid #ddd;">
                                                        <td style="font-size: 18px; font-weight: bold; color: #333; padding-top: 10px;">Total Amount:</td>
                                                        <td align="right" style="font-size: 20px; font-weight: bold; color: #667eea; padding-top: 10px;">LKR %.2f</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            
                            <!-- Delivery Info -->
                            <tr>
                                <td style="padding: 30px 20px 20px 20px;">
                                    <h3 style="color: #333; margin: 0 0 15px 0; font-size: 18px;">Delivery Information</h3>
                                    <div style="background-color: #f8f9fa; padding: 20px; border-radius: 5px; border-left: 4px solid #667eea;">
                                        <p style="margin: 0 0 10px 0; color: #333; font-weight: bold;">%s</p>
                                        <p style="margin: 0 0 5px 0; color: #666;">%s</p>
                                        <p style="margin: 0; color: #666;">%s</p>
                                    </div>
                                </td>
                            </tr>
                            
                            <!-- Status -->
                            <tr>
                                <td style="padding: 20px; text-align: center;">
                                    <div style="background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; border: 1px solid #c3e6cb;">
                                        <strong>✓ Payment Confirmed - Order Processing</strong>
                                    </div>
                                </td>
                            </tr>
                            
                            <!-- Footer -->
                            <tr>
                                <td style="padding: 30px 20px; text-align: center; background-color: #f8f9fa;">
                                    <p style="color: #666; margin: 0 0 15px 0; font-size: 16px;">We'll deliver your order within 2-3 business days.</p>
                                    <p style="color: #333; margin: 0; font-size: 16px;">Thank you for choosing <strong>CeyBazaar</strong>!</p>
                                </td>
                            </tr>
                            
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """, order.getOrderId(), order.getOrderDetails().getCustomerName(), 
            orderItemsHtml.toString(), 
            (order.getTotalCost() - order.getShippingCost()), order.getShippingCost(), order.getTotalCost(),
            order.getOrderDetails().getCustomerName(), 
            order.getOrderDetails().getAddress(), 
            order.getOrderDetails().getRegion());
                                
    }

    public static String generateRandomID() {
        Random random = new Random();
        int randomNumber = random.nextInt(1_000_000); // Generates a number between 0 and 999999
        return String.format("D%06d", randomNumber); // Adds 'D' prefix and ensures 6-digit format
    }


}
