package org.light32.projects.magicbus;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.light32.projects.magicbus.model.MBMessage;
import org.light32.projects.magicbus.model.MBStatus;
import org.light32.projects.magicbus.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



@SpringBootApplication
@RestController
@RequestMapping(value="/mb")
public class MagicBus {
	private static Properties kafkaProperties;
	private static Properties mbProperties;
	private static Producer<String, String> kProducer;
	private static Integer partition;
	private static final Logger log = LoggerFactory.getLogger(MagicBus.class);
	private static String defaultTopic;
	
	public static void main(String[] args) {
		initializeConfig(args);
		initializeProducer();		
        SpringApplication.run(MagicBus.class, args);
    }
    
 	@RequestMapping(method = RequestMethod.GET, value="/{topic}")
 	public String get(@PathVariable String topic) {
 		MBMessage message = new MBMessage();
 		message.setBody(null);
 		message.setRequestMethod("get");
 		String msgStr = JsonUtils.toJSON(message);

        produceMsg(topic, msgStr);

        log.debug("     : " + msgStr);
        MBStatus out = new MBStatus("OK");
        return JsonUtils.toJSON(out);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{topic}")
    public String post(@PathVariable String topic, @RequestBody Object request) {

 		MBMessage message = new MBMessage();
 		message.setBody(request);
 		message.setRequestMethod("post");
 		String msgStr = JsonUtils.toJSON(message);

        produceMsg(topic, msgStr);

 		log.debug("      : " + msgStr);

 		MBStatus out = new MBStatus("OK");
 		return JsonUtils.toJSON(out);
 	}

    private void produceMsg(String topic, String msgStr) {
        if (partition != null) {
            kProducer.send(new ProducerRecord<>(topic, partition, null, msgStr));
            log.info("GET -> " + topic + ":" + partition + " [" + msgStr.length() + "]");
        } else {
            kProducer.send(new ProducerRecord<>(topic, msgStr));
            log.info("GET -> " + topic + " [" + msgStr.length() + "]");
        }
    }

    //
    ////////////////////////////////////////////////////////////////////////////
    //

    // TODO make embedded tomcat work for PUT and DELETE
 	@RequestMapping(method = RequestMethod.DELETE, value = "/{topic}/{id}")
 	public @ResponseBody void delete(@PathVariable String topic, @PathVariable String id) {
 		MBMessage message = new MBMessage();
 		message.setId(id);
 		message.setBody(null);
 		message.setRequestMethod("delete");
 			
 		// hit kafka
        if (partition != null) {
            kProducer.send(new ProducerRecord<>(topic, partition, null, JsonUtils.toJSON(message)));
        } else {
            kProducer.send(new ProducerRecord<>(topic, JsonUtils.toJSON(message)));
        }
    }

 	@RequestMapping(method = RequestMethod.PUT, value = "/{topic}/{id}")
 	public @ResponseBody Object put(@PathVariable String topic, @PathVariable String id, @RequestBody Object request) {
		MBMessage message = new MBMessage();
 		message.setId(id);
 		message.setBody(request);
 		message.setRequestMethod("put");
 		
	 	// hit kafka
        if (partition != null) {
            kProducer.send(new ProducerRecord<>(topic, partition, null, JsonUtils.toJSON(message)));
        } else {
            kProducer.send(new ProducerRecord<>(topic, JsonUtils.toJSON(message)));
        }

        MBStatus out = new MBStatus("OK");
 		return JsonUtils.toJSON(out);
 	}
 	
 	
	private static void initializeProducer() {
		kafkaProperties = new Properties();
		kafkaProperties.put("bootstrap.servers", mbProperties.getProperty("kafka.server"));
		kafkaProperties.put("acks", "all");
		kafkaProperties.put("retries", 0);
		kafkaProperties.put("batch.size", 16384);
		kafkaProperties.put("linger.ms", 1);
		kafkaProperties.put("buffer.memory", 33554432);
		kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        kProducer = new KafkaProducer<>(kafkaProperties);
    }

	private static void initializeConfig(String[] args) {
		if(args.length == 0 || args[0] == null) {
			System.err.println("Usage: java magicbus.jar <properties file name> [partition]");
			System.exit(1);
		}
		if (args.length > 1 && args[1] != null) { 
			partition = Integer.parseInt(args[1]);
		}
		
		// load mb.properties file
		String mbPropFileName = args[0];		
		mbProperties = new Properties();

		try {
			InputStream propfileStream = new FileInputStream(mbPropFileName);
			mbProperties.load(propfileStream);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}
		
		defaultTopic = mbProperties.getProperty("default.topic");
	}

}