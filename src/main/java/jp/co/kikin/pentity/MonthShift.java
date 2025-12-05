package jp.co.kikin.pentity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="m_employee")
@Data
public class MonthShift {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employeeId")
//  /** 社員ID */
	private String employeeId;

//    /** 社員名 */
//    private String employeeName;
//    /** 1日 */
//    private String symbol01;
//    /** 2日 */
//    private String symbol02;
//    /** 3日 */
//    private String symbol03;
//    /** 4日 */
//    private String symbol04;
//    /** 5日 */
//    private String symbol05;
//    /** 6日 */
//    private String symbol06;
//    /** 7日 */
//    private String symbol07;
//    /** 8日 */
//    private String symbol08;
//    /** 9日 */
//    private String symbol09;
//    /** 10日 */
//    private String symbol10;
//    /** 11日 */
//    private String symbol11;
//    /** 12日 */
//    private String symbol12;
//    /** 13日 */
//    private String symbol13;
//    /** 14日 */
//    private String symbol14;
//    /** 15日 */
//    private String symbol15;
//    /** 16日 */
//    private String symbol16;
//    /** 17日 */
//    private String symbol17;
//    /** 18日 */
//    private String symbol18;
//    /** 19日 */
//    private String symbol19;
//    /** 20日 */
//    private String symbol20;
//    /** 21日 */
//    private String symbol21;
//    /** 22日 */
//    private String symbol22;
//    /** 23日 */
//    private String symbol23;
//    /** 24日 */
//    private String symbol24;
//    /** 25日 */
//    private String symbol25;
//    /** 26日 */
//    private String symbol26;
//    /** 27日 */
//    private String symbol27;
//    /** 28日 */
//    private String symbol28;
//    /** 29日 */
//    private String symbol29;
//    /** 30日 */
//    private String symbol30;
//    /** 31日 */
//    private String symbol31;
	
}

