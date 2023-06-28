package com.example.demo.post.infrastructure;

import com.example.demo.post.service.port.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository repository;

    public PostRepositoryImpl(PostJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PostEntity> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public PostEntity save(PostEntity postEntity) {
        return repository.save(postEntity);
    }
}
