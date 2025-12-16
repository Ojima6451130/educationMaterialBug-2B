package jp.co.kikin.pentity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_news")
@Data
public class NewsRecode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "employee_id")
	private String employeeId;

	@Column(name = "news_priority")
	private String newsPriority;

	@Column(name = "memo")
	private String memo;

	@Column(name = "update_datetime")
	private LocalDateTime updateDatetime;

}
