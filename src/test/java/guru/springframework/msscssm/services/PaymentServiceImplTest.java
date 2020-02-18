package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() {
        this.payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = this.paymentService.newPayment(payment);

        assertEquals(PaymentState.NEW, savedPayment.getState());

        StateMachine<PaymentState, PaymentEvent> sm = this.paymentService.preAuth(savedPayment.getId());

        Payment preAuthPayment = this.paymentRepository.getOne(savedPayment.getId());
        assertEquals(PaymentState.PRE_AUTH, preAuthPayment.getState());
        assertEquals(PaymentState.PRE_AUTH, sm.getState().getId());
    }
}