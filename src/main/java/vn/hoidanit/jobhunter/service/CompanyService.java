package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRespository;

@Service
public class CompanyService {
    private final CompanyRespository companyRespository;

    public CompanyService(CompanyRespository companyRespository) {
        this.companyRespository = companyRespository;
    }

    public Company handleSaveCompany(Company company) {
        return this.companyRespository.save(company);
    }

    public List<Company> getAllCompanies() {
        return this.companyRespository.findAll();
    }

    public ResultPaginationDTO getAllCompanies(Pageable pageable, Specification<Company> companySpec) {
        Page<Company> companiesPageable = companyRespository.findAll(companySpec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(companiesPageable.getNumber() + 1); // current Page
        meta.setPageSize(companiesPageable.getSize()); // element per page

        meta.setPages(companiesPageable.getTotalPages()); // get tottal page
        meta.setTotal(companiesPageable.getTotalElements()); // total element

        result.setMeta(meta);
        result.setResult(companiesPageable.getContent());

        return result;
    }

    public Company updateCompany(Company company) {
        return this.companyRespository.save(company);
    }

    public void deleteCompany(long id) {
        Company c = new Company();
        c.setId(id);
        this.companyRespository.delete(c);
    }

}
