package kr.dja.scaleSerialReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScaleDataFactory implements ISerialDTOFactory
{
	public static final String ERR_CODE_TIMEOUT = "13";
	public static final String ERR_CODE_NEGATIVE_VALUE = "20";

	@Override
	public ScaleDTO createDTO(String msg)
	{
		ScaleDTO dto = new ScaleDTO();
		
		try
		{
			dto.setErrorCode(msg.substring(0, 2));
			
			if(msg.length() > 2)
			{
				dto.setScaleStatus(msg.substring(2, 4));
				dto.setIdentNo(Integer.parseInt(msg.substring(17, 21).replace("_", "")));
				dto.setScaleNo(Integer.parseInt(msg.substring(21, 22).replace("_", "")));
				dto.setGross(Double.parseDouble(msg.substring(22, 30).replace("_", "")));
				dto.setTare(Double.parseDouble(msg.substring(30, 38).replace("_", "")));
				dto.setNet(Double.parseDouble(msg.substring(38, 46).replace("_", "")));
				dto.setUnit(msg.substring(46, 48).replace("_", ""));
				dto.setTairCode(msg.substring(48, 50));
				dto.setWeighingRange(msg.substring(50, 51));
				dto.setTerminalNo(msg.substring(51, 54));
			}

		}
		catch(Exception e)
		{
			dto = null;
			log.warn("parse error", e);
		}
		
		return dto;
	}
}
