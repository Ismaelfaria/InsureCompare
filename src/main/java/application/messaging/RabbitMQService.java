package application.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import application.messaging.dto.PolicyApprovalMessageRequest;
import application.messaging.dto.PolicyApprovalMessageResponse;

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.response.queue}")
    private String responseQueue;

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.request.queue}")
    public void receiveMessage(PolicyApprovalMessageRequest request) {
    	 System.out.println("Mensagem recebida da fila: " + request.getPolicyId());

    	    PolicyApprovalMessageResponse response = new PolicyApprovalMessageResponse(
    	        request.getPolicyId(),             
    	        request.getPolicyHolderNumber(),     
    	        request.getPolicyStatus(),         
    	        true                               
    	    );

    	    rabbitTemplate.convertAndSend(responseQueue, response);

    	    System.out.println("Resposta enviada para a fila: " + response.getPolicyId());
    }
}