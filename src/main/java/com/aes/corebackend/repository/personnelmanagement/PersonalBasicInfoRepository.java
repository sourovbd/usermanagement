package com.aes.corebackend.repository.personnelmanagement;

import com.aes.corebackend.entity.personnelmanagement.PersonalBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalBasicInfoRepository extends JpaRepository<PersonalBasicInfo, Long> {
    PersonalBasicInfo findPersonalBasicInfoByUserId(Long userId);
}