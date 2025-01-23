package application.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import application.messaging.dto.PolicyApprovalMessageRequest;

@Service
public class PolicyApprovalService {

	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange}")
	private String exchange;

	@Value("${rabbitmq.routing-key.request}")
	private String routingKeyRequest;

	public PolicyApprovalService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendApprovalRequest(PolicyApprovalMessageRequest request) {
		try {
			System.out.println("Enviando solicitação para o RabbitMQ...");
			rabbitTemplate.convertAndSend(exchange, routingKeyRequest, request);
		} catch (Exception e) {
			System.err.println("Erro ao enviar mensagem para o RabbitMQ: " + e.getMessage());
			e.printStackTrace();
		}
	}
}