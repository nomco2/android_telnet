package database;

public class InfoClass_algorithm {
	public int _id;
	private int function_type; //기능의 종류 : 디지털 신호, 아날로그 신호, if, for 등
	private String function_data; //if for 등 string 저장하고 각자에 맞춰서 파싱해서 사용
	private int algorithm_continuous; //이전 layout_id 번호
	private int layout_id; //layout상에 부여한 id


	/*
	예를 들어 function type = if 이고
	condtion type 이 1(센서 읽은 데이터 : 디지털) 이면
	if_compared_data 는 디지털 read 할 핀 번호를 넣으면 됨 -> 이때 compared_data는

	 */


	public InfoClass_algorithm(int _id , int function_type, String function_data, int algorithm_continuous, int layout_id){
		this._id = _id;
		this.function_type = function_type;
		this.function_data = function_data;
		this.algorithm_continuous = algorithm_continuous;
		this.layout_id = layout_id;
	}
	
}
