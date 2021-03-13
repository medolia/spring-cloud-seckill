package zone.medolia.web.kafka.service;

/*import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class ProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMsg(String topic, Object obj) {
        ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, obj);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("生产者发送消息失败，原因：{}",
                        throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("生产者成功发送消息到topic:{} partition:{}的消息队列",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition());
            }
        });
    }
}*/
