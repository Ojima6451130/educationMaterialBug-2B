package jp.co.kikin.prepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.kikin.pentity.SalaryInfoEntity;

@Repository
public interface SalaryInfoRepository extends JpaRepository<SalaryInfoEntity, String>{

}
