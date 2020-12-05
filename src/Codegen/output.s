	.text
	.globl asm_main
asm_main:
	pushq %rbp
	movq %rsp, %rbp
	movq $8, %rdi
	call mjcalloc
	leaq Person$$, %rdx
	movq %rdx, 0(%rax)
	movq %rax, %rdi
	pushq %rdi
	popq %rdi
	movq 0(%rdi), %rax
	subq $8, %rsp
	call *8(%rax)
	addq $8, %rsp
	movq %rax, %rdi
	call put
	movq %rbp, %rsp
	popq %rbp
	ret
Person$controlFlowTest:
	pushq %rbp
	movq %rsp, %rbp
	subq $16, %rsp
	movq $7, %rax
	movq %rax, -16(%rbp)
	movq $1, %rax
	movq %rax, -8(%rbp)
# Entering while loop.
	jmp .Test0
.Test0:
	movq -8(%rbp), %rax
	pushq %rax
	movq $10, %rax
	popq %rdx
	cmpq %rax, %rdx
	setl %al
	movzbq %al, %rax
	pushq %rax
	movq -16(%rbp), %rax
	pushq %rax
	movq -8(%rbp), %rax
	popq %rdx
	cmpq %rax, %rdx
	setl %al
	movzbq %al, %rax
	testq %rax, %rax
	sete %al
	movzbq %al, %rax
	popq %rdx
	cmpq %rdx, %rax
	sete %al
	movzbq %al, %rax
	cmpq $0, %rax
	je .Done0
	movq -8(%rbp), %rax
	pushq %rax
	movq $1, %rax
	popq %rdx
	addq %rdx, %rax
	movq %rax, -8(%rbp)
	jmp .Test0
.Done0:
	movq -8(%rbp), %rax
	movq %rbp, %rsp
	popq %rbp
	ret
Person$incrementAge:
	pushq %rbp
	movq %rsp, %rbp
	subq $16, %rsp
	pushq %rsi
	movq $30, %rax
	movq %rax, -8(%rbp)
	movq -8(%rbp), %rax
	pushq %rax
	movq -24(%rbp), %rax
	popq %rdx
	addq %rdx, %rax
	movq %rax, -16(%rbp)
	movq -16(%rbp), %rax
	movq %rax, %rdi
	call put
	movq $1, %rax
	movq %rbp, %rsp
	popq %rbp
	ret
Person$testB:
	pushq %rbp
	movq %rsp, %rbp
	movq $2020, %rax
	movq %rax, %rdi
	call put
	movq $1, %rax
	movq %rbp, %rsp
	popq %rbp
	ret
	.data
Person$$: .quad 0
	.quad Person$controlFlowTest
	.quad Person$incrementAge
	.quad Person$testB
