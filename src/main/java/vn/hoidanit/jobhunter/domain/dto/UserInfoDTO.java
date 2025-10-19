package vn.hoidanit.jobhunter.domain.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.utils.constant.GenderEnum;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private Instant updatedAt;

    public UserInfoDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.address = user.getAddress();
        this.age = user.getAge();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}
