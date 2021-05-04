package kr.dja.scaleSerialReader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 저울 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScaleDTO
{
	private long sequence;
	
	private String errorCode;
	
	private String scaleStatus;
	
	private int identNo;
	
	private int scaleNo;
	
	private double gross;
	
	private double tare;
	
	private double net;
	
	private String unit;
	
	private String tairCode;
	
	private String weighingRange;
	
	private String terminalNo;
}
