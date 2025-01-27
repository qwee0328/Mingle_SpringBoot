package com.mingle.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mingle.domain.entites.Post;

import jakarta.transaction.Transactional;

public interface PostRepository extends JpaRepository<Post, Long>{
	
    @Query("select p from Post p " +
            "left join fetch p.member " +
            "left join fetch p.files " +
            "left join fetch p.replies r " +
            "left join fetch r.childrenReplies " +
            "where r.parentReply is null and p.id = :id")
    Post findPostById(@Param("id") Long id);
	

	@Query("select p from Post p left join fetch p.member left join fetch p.replies left join fetch p.files where p.isNotice = true")
	List<Post> findAllByNoticePosts();
	
	// 게시글 조회 수 증가
	@Transactional
	@Modifying
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
	public void incrementViewCount(@Param("postId") Long postId);
	
	// 게시글 조회 수 출력
	@Query("select p.viewCount from Post p where p.id = :postId")
	public Long selectViewcountByPostId(@Param("postId") Long postId);
	
	// 고정 중인 공지글 오름차순으로 출력
	@Query("select p from Post p where isNotice = true and isFix = true order by writeDate desc")
	List<Post> selectByFixedNotice();
	
	// 고정 중이 아닌 공지글 오름차순으로 출력
	@Query("select p from Post p where isNotice = true and isFix = false order by writeDate desc")
	List<Post> selectByUnFixedNotice();
	
	// 아이디에 해당하는 Post
	Post findAllById(@Param("id") Long id);
	
}
