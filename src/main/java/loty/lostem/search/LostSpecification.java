package loty.lostem.search;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.entity.Post;
import loty.lostem.entity.PostLost;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LostSpecification {
    /*public static Specification<Post> withFilters(
            String keyword,
            String type,
            String area,
            String periodStart,
            String periodEnd
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + keyword + "%"));
            }
            if (area != null && !area.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("area"), area));
            }
            if (periodStart != null && periodEnd != null &&
                    !periodStart.isEmpty() && !periodEnd.isEmpty()) {
                predicates.add(criteriaBuilder.between(root.get("period"), periodStart, periodEnd));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }*/

    ///
    /*public interface Specification<T> {
        Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb);
    }

    public static Specification<PostLost> findByCategory (String keyword) {
        return new Specification<PostLost>() {
            @Override
            public Predicate toPredicate(Root<PostLost> root, CriteriaQuery query, CriteriaBuilder cb) {
                return cb.equal(root.get("category"), keyword);
            }
        };
    }
    public static Specification<PostLost> betweenPeriod(String keyword) {
        return new Specification<PostLost>() {
            @Override
            public Predicate toPredicate(Root<PostLost> root, CriteriaQuery query, CriteriaBuilder cb) {
                return cb.between(root.get("period"), "a", keyword);
            }
        };
    }*/
    /*public static Specification<PostDTO> equalStatus(String status) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("status"), StatusEnum.valueOf(status));
    }*/
    public static Specification<PostLost> likeTitle(String title) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("title"),"%" + title + "%");
    }
    public static Specification<PostLost> equalCategory(String category) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("category"), category);
    }
    public static Specification<PostLost> equalDate(LocalDateTime date) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("date"), date);
    }
    public static Specification<PostLost> equalArea(String area) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("area"), area);
    }
    public static Specification<PostLost> likePlace(String place) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("place"), "%" + place + "%");
    }
    public static Specification<PostLost> equalItem(String item) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("item"), item);
    }
    public static Specification<PostLost> likeContents(String contents) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("contents"), "%" + contents + "%");
    }
    public static Specification<PostLost> equalState(String state) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("state"), state);
    }
 }
