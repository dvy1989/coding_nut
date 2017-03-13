package nut.json;

import java.util.Collection;

public class MontlyWageReportJsonModel extends BasicJsonResponseModel {
	public int RecordsBefore;
	public int RecordsAfter;
	public Collection<BasicWageInfoJsonModel> WageInfo;
	
	public MontlyWageReportJsonModel(){
		
	}
}
