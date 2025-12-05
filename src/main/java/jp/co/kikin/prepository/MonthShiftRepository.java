package jp.co.kikin.prepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.kikin.pentity.MonthShift;

@Repository
public interface MonthShiftRepository extends JpaRepository<MonthShift, String> {

}
