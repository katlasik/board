package katlasik.board.repositories;

import katlasik.board.dtos.QuestionView;
import katlasik.board.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long>, JpaRepository<Question, Long> {

    @Query("select q from Question q left join fetch q.images i left join fetch q.user u left join fetch q.answers a left join fetch a.user where q.id = ?1 order by q.createdOn")
    Optional<Question> findWithAnswers(Long id);

    @Query("select new katlasik.board.dtos.QuestionView(q.id, q.title, size(a)) from Question q left join q.answers a group by q.id, q.title order by q.createdOn desc ")
    Page<QuestionView> findQuestionViews(Pageable page);

    @Query("select new katlasik.board.dtos.QuestionView(q.id, q.title, size(a)) from Question q left join q.answers a where q.user.id = ?1 group by q.id, q.title order by q.createdOn desc ")
    Page<QuestionView> findQuestionViewsById(long usedId, Pageable page);
}
