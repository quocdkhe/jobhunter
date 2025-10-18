package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UserInfoDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.utils.error.EmailExistException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserInfoDTO createUser(User user) throws EmailExistException {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistException();
        }
        User newUser = this.userRepository.save(user);
        return new UserInfoDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getGender(),
                newUser.getAddress(),
                newUser.getAge(), newUser.getCreatedAt());
    }

    public User updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        return this.userRepository.save(user);
    }

    public ResultPaginationDTO getAllUsers(Pageable pageable, Specification<User> userSpec) {
        Page<User> usersPagable = userRepository.findAll(userSpec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1); // current Page
        meta.setPageSize(pageable.getPageSize()); // element per page

        meta.setPages(usersPagable.getTotalPages()); // get tottal page
        meta.setTotal(usersPagable.getTotalElements()); // total element

        result.setMeta(meta);
        result.setResult(usersPagable.getContent());

        return result;
    }

    public User getById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.isPresent() ? userOptional.get() : null;
    }

    public boolean checkUserById(long id) {
        return this.userRepository.existsById(id);
    }

    public User getByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

}
