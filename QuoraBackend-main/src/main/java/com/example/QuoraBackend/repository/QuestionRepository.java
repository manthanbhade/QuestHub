//package com.example.QuoraBackend.repository;
//
//import com.example.QuoraBackend.model.Question;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//
//@Repository
//public interface QuestionRepository extends JpaRepository<Question,Long> {
//
//    List<Question> findByUser(Long user);
//
//
//}

package com.example.QuoraBackend.repository;

import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByUser(User user);

}

