package com.bettervns.teachersservice;

import com.bettervns.ddlservice.models.Teacher;
import com.bettervns.ddlservice.repository.TeacherRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication
public class TeacherserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherserviceApplication.class, args);
    }

    @Bean
    public TeacherRepository teacherRepository(){
        return new TeacherRepository() {
            @Override
            public void flush() {

            }

            @Override
            public <S extends Teacher> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends Teacher> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<Teacher> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Integer> integers) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Teacher getOne(Integer integer) {
                return null;
            }

            @Override
            public Teacher getById(Integer integer) {
                return null;
            }

            @Override
            public Teacher getReferenceById(Integer integer) {
                return null;
            }

            @Override
            public <S extends Teacher> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Teacher> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public <S extends Teacher> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public List<Teacher> findAll() {
                return null;
            }

            @Override
            public List<Teacher> findAllById(Iterable<Integer> integers) {
                return null;
            }

            @Override
            public <S extends Teacher> S save(S entity) {
                return null;
            }

            @Override
            public Optional<Teacher> findById(Integer integer) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Integer integer) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Integer integer) {

            }

            @Override
            public void delete(Teacher entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Integer> integers) {

            }

            @Override
            public void deleteAll(Iterable<? extends Teacher> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public List<Teacher> findAll(Sort sort) {
                return null;
            }

            @Override
            public Page<Teacher> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Teacher> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Teacher> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Teacher> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Teacher> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends Teacher, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }
        };
    }

}
