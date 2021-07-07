package kr.dja.scaleSerialReader;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerialMessageReaderImpl implements ISerialMessageReader
{

	private ISerialDataObserver observer;
	private final LinkedList<Byte> byteBuffer;
	private int recvEndFlag = 0;
	private final ExecutorService executor;
	private final ISerialDTOFactory dataFactory;
	
	public SerialMessageReaderImpl(ISerialDTOFactory dataFactory)
	{
		this.byteBuffer = new LinkedList<Byte>();
		this.dataFactory = dataFactory;
		this.executor = Executors.newSingleThreadExecutor();
	}

	@Override
	public void readData(byte[] serialData)
	{

		for (int index = 0; index < serialData.length; ++index)
		{
			byte b = serialData[index];
			if (this.byteBuffer.size() > 1024) this.byteBuffer.removeFirst();
			
			this.byteBuffer.add(b);
			switch (b)
			{
			case '>':
				this.recvEndFlag = 1;
				break;
			case '\r':
				if (this.recvEndFlag == 1)
					this.recvEndFlag = 2;
				else
					this.recvEndFlag = 0;
				break;
			case '\n':
				if (this.recvEndFlag == 2)
					this.recvEndFlag = 3;
				else
					this.recvEndFlag = 0;
				break;
			default:
				this.recvEndFlag = 0;
			}
			if (this.recvEndFlag == 3)
			{
				this.recvEndFlag = 0;
				this.byteBuffer.pollLast();
				this.byteBuffer.pollLast();
				this.byteBuffer.pollLast();
				byte dataChar;
				StringBuilder sb = new StringBuilder();
				while (true)
				{
					if(this.byteBuffer.isEmpty()) break;
					dataChar = this.byteBuffer.pollLast();
					if (dataChar == '<') break;
					sb.append((char) dataChar);
				}
				this.byteBuffer.clear();
				sb.reverse();
				if(this.observer != null)
				{

					Object dto = this.dataFactory.createDTO(sb.toString());
					if(dto != null)
					{
						this.observer.update(dto);
					}
				}
			}
		}
	}

	@Override
	public void attachObserver(ISerialDataObserver observer)
	{
		this.observer = observer;
		
	}

}
