package jp.co.kikin.prepository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.kikin.pentity.NewsRecode;

public interface NewsRepository extends JpaRepository<NewsRecode, Integer> {

}
