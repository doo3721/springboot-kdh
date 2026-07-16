package net.likelion.bebc25.board03.post.repository;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.board03.post.dto.PostDto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PureJdbcPostRepository implements PostRepository {
    private String url = "jdbc:mysql://localhost:3306/board_db?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private String user = "user1";
    private String password = "1111";

    @Override
    public List<PostDto> findAll() {
        String sql = "SELECT id, title, author, secret, created_at AS createdAt FROM post2";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<PostDto> result = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, password);
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();

            while (rs.next()) {
                PostDto post = new PostDto();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setAuthor(rs.getString("author"));
                post.setSecret(rs.getBoolean("secret"));
                post.setCreatedAt(rs.getObject("createdAt", LocalDateTime.class));

                result.add(post);
            }
        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
            try { if (stat != null) stat.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
            try { if (conn != null) conn.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public PostDto findById(int id) throws IllegalArgumentException {
        String sql = "SELECT id, title, author, created_at, content FROM post2 WHERE id = " + id;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PostDto post = new PostDto();

        try {
            conn = DriverManager.getConnection(url, user, password);
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();

            if (rs.next()) {
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setAuthor(rs.getString("author"));
                post.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                post.setContent(rs.getString("content"));
            }
            else {
                log.error("해당 글을 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
            try { if (stat != null) stat.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
            try { if (conn != null) conn.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
        }
        return post;
    }

    @Override
    public void save(PostDto post) {
        Connection conn = null;
        PreparedStatement pStat = null;
        String sql = "INSERT INTO post2 (author, title, content) VALUES (?, ?, ?)";

        try {
            conn = DriverManager.getConnection(url, user, password);
            pStat = conn.prepareStatement(sql);
            pStat.setString(1, post.getAuthor());
            pStat.setString(2, post.getTitle());
            pStat.setString(3, post.getContent());
            int affectedRows = pStat.executeUpdate();

            log.debug("글 등록 완료: " + affectedRows + "건 반영됨");
        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (pStat != null) pStat.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
            try { if (conn != null) conn.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(PostDto post) {
        Connection conn = null;
        PreparedStatement pStat = null;
        String sql = "UPDATE post2 SET author = ?, title = ?, content = ? WHERE id = ?";

        try {
            conn = DriverManager.getConnection(url, user, password);
            pStat = conn.prepareStatement(sql);
            pStat.setString(1, post.getAuthor());
            pStat.setString(2, post.getTitle());
            pStat.setString(3, post.getContent());
            pStat.setInt(4, post.getId());
            int affectedRows = pStat.executeUpdate();

            log.debug("글 등록 완료: " + affectedRows + "건 반영됨");
        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (pStat != null) pStat.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
            try { if (conn != null) conn.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteById(int id) {
        Connection conn = null;
        PreparedStatement pStat = null;
        String sql = "DELETE FROM post2 WHERE id = ?";

        try {
            conn = DriverManager.getConnection(url, user, password);
            pStat = conn.prepareStatement(sql);
            pStat.setInt(1, id);
            int affectedRows = pStat.executeUpdate();

            log.debug("글 삭제 완료: " + affectedRows + "건 반영됨");
        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (pStat != null) pStat.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
            try { if (conn != null) conn.close(); } catch (Exception e) {
                log.error("에러 발생: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
