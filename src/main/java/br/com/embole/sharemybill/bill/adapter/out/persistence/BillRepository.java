package br.com.embole.sharemybill.bill.adapter.out.persistence;

import br.com.embole.sharemybill.bill.domain.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
}
