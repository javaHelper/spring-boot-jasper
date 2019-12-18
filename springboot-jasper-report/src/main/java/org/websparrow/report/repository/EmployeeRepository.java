package org.websparrow.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.websparrow.report.dto.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
