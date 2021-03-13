package zone.medolia.web.kafka.service;



/*@Service
@Slf4j
public class ConsumerService {

    @Value("${kafka.topic.login}")
    String login;

    @Value("${kafka.topic.goods}")
    String goods;

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = {"${kafka.topic.login}"}, groupId = "group1")
    public void consume0(ConsumerRecord<String, String> record) {
        try {
            Result result = mapper.readValue(record.value(), Result.class);
            log.info("登录结果为消息 -> {}", result.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}*/
