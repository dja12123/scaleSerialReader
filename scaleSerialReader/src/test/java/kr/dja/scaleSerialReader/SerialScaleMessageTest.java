package kr.dja.scaleSerialReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerialScaleMessageTest
{
	private ScaleDataFactory scaleDataFactory;
	private ISerialMessageReader serialMessageReader;

	public SerialScaleMessageTest()
	{
		scaleDataFactory = new ScaleDataFactory();
		serialMessageReader = new SerialMessageReaderImpl(scaleDataFactory);//<==ISerialMessageReader 구현할것
		serialMessageReader.attachObserver(dto -> log.info(dto.toString()));
	}
	
	@Test
	public void scaleMessageTest() throws InterruptedException
	{
		log.debug("test start");
		List<Object> generatedDTO = new ArrayList<>();
		serialMessageReader.attachObserver(generatedDTO::add);
		ScaleDTO dto = scaleDataFactory.createDTO("000002.05.0514:30___11__430.00___30.00__400.00g_PT2001___45678");
		serialMessageReader.readData("<000002.05.0514:30___11__430.00___30.00__400.00g_PT2001___45678>\r\n".getBytes());
		for(int i = 0; i < 10000; ++i)
		{
			serialMessageReader.readData("<000002.05.0514:30___11__430.00___30.00__400.00g_PT2001___4567".getBytes());
		}
		serialMessageReader.readData("<000002.05.0514:30___11__430.00___30.00__400.00g_PT2001___45678>\r\n<000002.05.0514:30___11__430.00___30.00__400.00g_PT2001___45678>\r\n".getBytes());
		for(int i = 0; i < 1000; ++i)
		{
			serialMessageReader.readData("<000002.05.0514:30___11__430.00___30.00__400.00g_PT2001___4567".getBytes());
		}
		serialMessageReader.readData("<000002.05.0514:30___11__430.aa___30.00__400.00g_PT2001___45678>\r\n".getBytes());
		serialMessageReader.readData("<aaa>\r\n".getBytes());
		serialMessageReader.readData("<aaa\n".getBytes());
		serialMessageReader.readData("aaa>\r\n".getBytes());
		serialMessageReader.readData("aaaa<000002.05.0514:30___11__430.00___30.00".getBytes());
		serialMessageReader.readData("__400.00g_PT2001___45678>\r\naaa".getBytes());
		serialMessageReader.readData("aaa>\r\n".getBytes());
		assertEquals(generatedDTO, List.of(dto,dto,dto,dto));
		log.debug("test finish");
	}
}
