package com.omnischool.service.impl;

import com.omnischool.entity.ContactMessage;
import com.omnischool.entity.DemoRequest;
import com.omnischool.enums.DemoStatus;
import com.omnischool.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Simple HTML mail sender.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String ADMIN_EMAIL = "admin@omnischool.tn";

    private final JavaMailSender mailSender;
    private final boolean mailEnabled;

    public EmailServiceImpl(JavaMailSender mailSender, @Value("${app.mail.enabled:true}") boolean mailEnabled) {
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        log.info("EmailService initialized - Mail enabled: {}", mailEnabled);
    }

    @Async
    @Override
    public void sendDemoRequestConfirmation(DemoRequest request) {
        String subject = "✓ Demande de démo reçue - OmniSchool";
        String body = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@700;800&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
                </head>
                <body style="margin:0;padding:0;background-color:#F9F9F7;font-family:'Inter',Arial,sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#F9F9F7;padding:40px 20px;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.08);">
                                    <!-- Header -->
                                    <tr>
                                        <td style="background-color:#2D472C;padding:32px;text-align:center;">
                                            <h1 style="margin:0;font-family:'Montserrat',Arial,sans-serif;font-size:28px;font-weight:800;color:#ffffff;letter-spacing:-0.5px;">OmniSchool</h1>
                                            <p style="margin:8px 0 0 0;font-size:13px;color:#C5A059;font-weight:600;letter-spacing:0.1em;text-transform:uppercase;">Plateforme de Gestion Scolaire</p>
                                        </td>
                                    </tr>
                                    <!-- Content -->
                                    <tr>
                                        <td style="padding:40px 32px;">
                                            <h2 style="margin:0 0 16px 0;font-family:'Montserrat',Arial,sans-serif;font-size:24px;font-weight:700;color:#2D472C;">Bonjour %s,</h2>
                                            <p style="margin:0 0 16px 0;font-size:15px;line-height:1.6;color:#374151;">Merci pour votre intérêt pour <strong>OmniSchool</strong>!</p>
                                            <p style="margin:0 0 24px 0;font-size:15px;line-height:1.6;color:#374151;">Nous avons bien reçu votre demande de démonstration pour <strong style="color:#2D472C;">%s</strong>.</p>
                                            
                                            <div style="background-color:#F9F9F7;border-left:4px solid #C5A059;padding:16px 20px;margin:24px 0;border-radius:4px;">
                                                <p style="margin:0;font-size:14px;line-height:1.5;color:#6B7280;">📅 Notre équipe vous contactera dans les <strong>24-48 heures</strong> pour planifier votre démonstration personnalisée.</p>
                                            </div>
                                            
                                            <p style="margin:24px 0 0 0;font-size:15px;line-height:1.6;color:#374151;">À très bientôt,</p>
                                            <p style="margin:8px 0 0 0;font-size:15px;font-weight:600;color:#2D472C;">L'équipe OmniSchool</p>
                                        </td>
                                    </tr>
                                    <!-- Footer -->
                                    <tr>
                                        <td style="background-color:#F9F9F7;padding:24px 32px;text-align:center;border-top:1px solid #E5E7EB;">
                                            <p style="margin:0 0 8px 0;font-size:13px;color:#6B7280;">OmniSchool - Solution de gestion scolaire complète</p>
                                            <p style="margin:0;font-size:12px;color:#9CA3AF;">📧 contact@omnischool.tn | 🌐 www.omnischool.tn</p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(escape(request.getFullName()), escape(request.getSchoolName()));
        safeSend(request.getEmail(), subject, body);
    }

    @Async
    @Override
    public void sendAdminNotification(DemoRequest request) {
        String subject = "🔔 Nouvelle demande de démo - " + request.getSchoolName();
        String body = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@700;800&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
                </head>
                <body style="margin:0;padding:0;background-color:#F9F9F7;font-family:'Inter',Arial,sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#F9F9F7;padding:40px 20px;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.08);">
                                    <!-- Header -->
                                    <tr>
                                        <td style="background:linear-gradient(135deg, #2D472C 0%%, #1a2a1b 100%%);padding:32px;">
                                            <h1 style="margin:0 0 8px 0;font-family:'Montserrat',Arial,sans-serif;font-size:24px;font-weight:800;color:#ffffff;">🎯 Nouvelle Demande de Démo</h1>
                                            <p style="margin:0;font-size:13px;color:#C5A059;font-weight:600;">Tableau de bord administrateur</p>
                                        </td>
                                    </tr>
                                    <!-- Content -->
                                    <tr>
                                        <td style="padding:32px;">
                                            <div style="background-color:#F9F9F7;border-radius:8px;padding:20px;margin-bottom:24px;">
                                                <h2 style="margin:0 0 16px 0;font-family:'Montserrat',Arial,sans-serif;font-size:20px;font-weight:700;color:#2D472C;">%s</h2>
                                                <p style="margin:0;font-size:13px;color:#6B7280;text-transform:uppercase;letter-spacing:0.05em;font-weight:600;">%s • %s élèves</p>
                                            </div>
                                            
                                            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:24px;">
                                                <tr>
                                                    <td style="padding:12px 0;border-bottom:1px solid #E5E7EB;">
                                                        <p style="margin:0;font-size:12px;color:#6B7280;font-weight:600;text-transform:uppercase;letter-spacing:0.05em;">Contact</p>
                                                        <p style="margin:4px 0 0 0;font-size:15px;color:#2D472C;font-weight:600;">%s</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:12px 0;border-bottom:1px solid #E5E7EB;">
                                                        <p style="margin:0;font-size:12px;color:#6B7280;font-weight:600;text-transform:uppercase;letter-spacing:0.05em;">Email</p>
                                                        <p style="margin:4px 0 0 0;font-size:15px;color:#2D472C;"><a href="mailto:%s" style="color:#2D472C;text-decoration:none;">%s</a></p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:12px 0;border-bottom:1px solid #E5E7EB;">
                                                        <p style="margin:0;font-size:12px;color:#6B7280;font-weight:600;text-transform:uppercase;letter-spacing:0.05em;">Téléphone</p>
                                                        <p style="margin:4px 0 0 0;font-size:15px;color:#2D472C;"><a href="tel:%s" style="color:#2D472C;text-decoration:none;">%s</a></p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:12px 0;">
                                                        <p style="margin:0;font-size:12px;color:#6B7280;font-weight:600;text-transform:uppercase;letter-spacing:0.05em;">Message</p>
                                                        <p style="margin:8px 0 0 0;font-size:14px;line-height:1.6;color:#374151;">%s</p>
                                                    </td>
                                                </tr>
                                            </table>
                                            
                                            <a href="http://localhost:3000/admin/analytics" style="display:inline-block;background-color:#2D472C;color:#ffffff;text-decoration:none;padding:12px 24px;border-radius:6px;font-weight:600;font-size:14px;margin-top:8px;">📊 Voir dans le tableau de bord</a>
                                        </td>
                                    </tr>
                                    <!-- Footer -->
                                    <tr>
                                        <td style="background-color:#F9F9F7;padding:20px 32px;text-align:center;border-top:1px solid #E5E7EB;">
                                            <p style="margin:0;font-size:12px;color:#9CA3AF;">Notification automatique OmniSchool</p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                escape(request.getSchoolName()),
                request.getSchoolType() == null ? "" : request.getSchoolType().name(),
                request.getNumberOfStudents() == null ? "0" : request.getNumberOfStudents().toString(),
                escape(request.getFullName()),
                escape(request.getEmail()),
                escape(request.getEmail()),
                escape(request.getPhone()),
                escape(request.getPhone()),
                escape(request.getMessage())
        );
        safeSend(ADMIN_EMAIL, subject, body);
    }

    @Async
    @Override
    public void sendContactConfirmation(ContactMessage message) {
        String subject = "✓ Message reçu - OmniSchool";
        String body = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@700;800&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
                </head>
                <body style="margin:0;padding:0;background-color:#F9F9F7;font-family:'Inter',Arial,sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#F9F9F7;padding:40px 20px;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.08);">
                                    <tr>
                                        <td style="background-color:#2D472C;padding:32px;text-align:center;">
                                            <h1 style="margin:0;font-family:'Montserrat',Arial,sans-serif;font-size:28px;font-weight:800;color:#ffffff;">OmniSchool</h1>
                                            <p style="margin:8px 0 0 0;font-size:13px;color:#C5A059;font-weight:600;letter-spacing:0.1em;text-transform:uppercase;">Service Client</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:40px 32px;">
                                            <h2 style="margin:0 0 16px 0;font-family:'Montserrat',Arial,sans-serif;font-size:24px;font-weight:700;color:#2D472C;">Bonjour %s,</h2>
                                            <p style="margin:0 0 16px 0;font-size:15px;line-height:1.6;color:#374151;">Nous avons bien reçu votre message concernant :</p>
                                            <div style="background-color:#F9F9F7;border-left:4px solid #C5A059;padding:16px 20px;margin:16px 0 24px 0;border-radius:4px;">
                                                <p style="margin:0;font-size:15px;font-weight:600;color:#2D472C;">%s</p>
                                            </div>
                                            <p style="margin:0 0 16px 0;font-size:15px;line-height:1.6;color:#374151;">Notre équipe vous répondra dans les plus brefs délais.</p>
                                            <p style="margin:24px 0 0 0;font-size:15px;line-height:1.6;color:#374151;">Cordialement,</p>
                                            <p style="margin:8px 0 0 0;font-size:15px;font-weight:600;color:#2D472C;">L'équipe OmniSchool</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color:#F9F9F7;padding:24px 32px;text-align:center;border-top:1px solid #E5E7EB;">
                                            <p style="margin:0 0 8px 0;font-size:13px;color:#6B7280;">OmniSchool - Solution de gestion scolaire complète</p>
                                            <p style="margin:0;font-size:12px;color:#9CA3AF;">📧 contact@omnischool.tn | 🌐 www.omnischool.tn</p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(escape(message.getName()), escape(message.getSubject()));
        safeSend(message.getEmail(), subject, body);
    }

    @Async
    @Override
    public void sendDemoStatusUpdateNotification(DemoRequest request, DemoStatus oldStatus, DemoStatus newStatus) {
        String subject = "OmniSchool | Demo request update";
        String body = """
                <div style='font-family:Arial,sans-serif'>
                  <h2>Hello, %s</h2>
                  <p>Your demo request status changed from <b>%s</b> to <b>%s</b>.</p>
                  <p>School: <b>%s</b></p>
                </div>
                """.formatted(
                escape(request.getFullName()),
                oldStatus == null ? "" : oldStatus.name(),
                newStatus == null ? "" : newStatus.name(),
                escape(request.getSchoolName())
        );
        safeSend(request.getEmail(), subject, body);
    }

    private void safeSend(String to, String subject, String htmlBody) {
        if (!mailEnabled) {
            log.warn("Email sending is disabled. Would have sent email to: {} with subject: {}", to, subject);
            return;
        }
        try {
            log.info("Attempting to send email to: {} with subject: {}", to, subject);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom("contact@omnischool.tn", "OmniSchool");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException ex) {
            log.error("MessagingException while sending email to {}: {}", to, ex.getMessage(), ex);
        } catch (MailException ex) {
            log.error("MailException while sending email to {}: {}", to, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error while sending email to {}: {}", to, ex.getMessage(), ex);
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
