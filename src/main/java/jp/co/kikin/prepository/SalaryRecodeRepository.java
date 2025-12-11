package jp.co.kikin.prepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.kikin.pentity.SalaryRecodeEntity;

@Repository
public interface SalaryRecodeRepository extends JpaRepository<SalaryRecodeEntity, String> {

}
