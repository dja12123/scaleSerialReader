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
		
		
		return dto;
	}
}
