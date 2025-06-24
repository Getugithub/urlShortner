package mpesa.com.spring.project.repository;

import mpesa.com.spring.project.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, String> {
    Optional<UrlEntity> findByShortCode(String shortCode);

    Optional<UrlEntity> findByLongUrl(String longUrl);
}