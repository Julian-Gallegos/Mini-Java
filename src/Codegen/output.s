	.text
	.globl asm_main
asm_main:
	movq $8, %rdi
	call mjcalloc
	leaq Person$$(%rip), %rdx
	movq %rdx, 0(%rax)
	movq 0(%rbp), %rdi
	movq 0(%rdi), %rax
	call *8(%rax)
	movq %rax, %rdi
	call put
	.data
Person$testA:
	movq $401, %rax
	movq %rax, %rdi
	call put
	movq $1, %rax
Person$testB:
        movq $2020, %rax
        movq %rax, %rdi
        call put
        movq $1, %rax
	.data
Person$$: .quad 0
        .quad Person$testA
	.quad Person$testB