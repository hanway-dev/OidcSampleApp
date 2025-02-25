package org.example.oidcsampleapp.Repository;

import org.example.oidcsampleapp.Models.HUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    public Optional<HUser> findByEmail(String email) {
        // email 을 이용하여 DB에서 사용자 정보를 가져옵니다.
        // 여기서는 DB 에서 가져온 것처럼 가상으로 개체를 생성합니다.

        var user = new HUser();
        user.setName("John Doe");
        user.setEmail(email);

        return Optional.of(user);
    }
}
