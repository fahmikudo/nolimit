package id.nolimit.api.blogpost.repository;

import id.nolimit.api.blogpost.entity.Post;
import id.nolimit.api.blogpost.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUser(Long id, User user);

}
