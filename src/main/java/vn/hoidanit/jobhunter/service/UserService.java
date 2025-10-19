package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
        return new UserInfoDTO(newUser);
    }

    public UserInfoDTO updateUser(User user) throws NoResourceFoundException {
        Optional<User> currentUserOptional = userRepository.findById(user.getId());
        if (!currentUserOptional.isPresent()) {
            throw new NoResourceFoundException(HttpMethod.GET, "user with id: " + user.getId());
        }

        User currentUser = currentUserOptional.get();
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        currentUser.setAddress(user.getAddress());
        currentUser.setAge(user.getAge());
        currentUser.setName(user.getName());
        currentUser.setGender(user.getGender());

        User updatedUser = this.userRepository.save(currentUser);
        return new UserInfoDTO(updatedUser);
    }

    public ResultPaginationDTO<List<UserInfoDTO>> getAllUsers(Pageable pageable, Specification<User> userSpec) {
        Page<User> usersPagable = userRepository.findAll(userSpec, pageable);
        ResultPaginationDTO<List<UserInfoDTO>> result = new ResultPaginationDTO<>();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1); // current Page
        meta.setPageSize(pageable.getPageSize()); // element per page

        meta.setPages(usersPagable.getTotalPages()); // get tottal page
        meta.setTotal(usersPagable.getTotalElements()); // total element

        result.setMeta(meta);

        List<UserInfoDTO> userInfos = usersPagable.getContent().stream()
                .map(user -> new UserInfoDTO(user))
                .collect(Collectors.toList());
        result.setResult(userInfos);
        return result;
    }

    public UserInfoDTO getById(long id) throws NoResourceFoundException {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new NoResourceFoundException(HttpMethod.GET, "user with id: " + id);
        }
        User current = userOptional.get();
        return new UserInfoDTO(current);
    }

    public boolean checkUserById(long id) {
        return this.userRepository.existsById(id);
    }

    public User getByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public void deleteUser(long id) {

    }

}
