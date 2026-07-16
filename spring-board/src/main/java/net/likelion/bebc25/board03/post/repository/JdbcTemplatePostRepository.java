package net.likelion.bebc25.board03.post.repository;

import net.likelion.bebc25.board03.post.dto.PostDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcTemplatePostRepository implements PostRepository{
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PostDto> postDtoRowMapper = (ResultSet rs, int rowNum) -> {
//        return new PostDto(
//                rs.getInt("id"),
//                rs.getString("title"),
//                rs.getString("content"),
//                rs.getString("author"),
//                rs.getBoolean("secret"),
//                rs.getObject("created_at", LocalDateTime.class)
//        );
        // 생성자 호출 방식과 다르게 매개변수 순서 달라도 상관 없음
        return PostDto.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .secret(rs.getBoolean("secret"))
                .build();
    };

    public JdbcTemplatePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PostDto> findAll() {
        return jdbcTemplate.query("SELECT * FROM post2", postDtoRowMapper);
    }

    @Override
    public PostDto findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM post2 WHERE id = ?", postDtoRowMapper, id);
    }

    @Override
    public void save(PostDto post) {
        jdbcTemplate.update(
                "INSERT INTO post2 (title, author, content) VALUES (?, ?, ?)",
                post.getTitle(), post.getAuthor(), post.getContent()
        );
    }

    @Override
    public void update(PostDto post) {
        jdbcTemplate.update(
                "UPDATE post2 SET title = ?, author = ?, content = ? WHERE id = ?",
                post.getTitle(), post.getAuthor(), post.getContent(), post.getId()
        );
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM post2 WHERE id = ?", id);
    }
}
