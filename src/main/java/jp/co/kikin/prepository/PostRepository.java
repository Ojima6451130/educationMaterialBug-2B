package jp.co.kikin.prepository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.kikin.pentity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, String>{

}
