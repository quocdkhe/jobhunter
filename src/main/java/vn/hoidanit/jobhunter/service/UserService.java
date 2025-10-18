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
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUpdateUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

    // public List<User> getAllUsers(Pageable pageable) {
    // Page<User> usersPagable = userRepository.findAll(pageable);
    // return usersPagable.getContent();
    // }

    public ResultPaginationDTO getAllUsers(Pageable pageable, Specification<User> userSpec) {
        Page<User> usersPagable = userRepository.findAll(userSpec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(usersPagable.getNumber() + 1); // current Page
        meta.setPageSize(usersPagable.getSize()); // element per page

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
