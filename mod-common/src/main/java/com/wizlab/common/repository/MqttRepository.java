package com.wizlab.common.repository;

import com.wizlab.common.domain.TpmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MqttRepository extends JpaRepository<TpmsEntity, Long> {

//    TpmsEntity findOne(Long id);
//    List<TpmsEntity> findAll();
}
