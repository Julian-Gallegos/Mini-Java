	.text
	.globl asm_main
asm_main:
	movq $8, %rdi
	call mjcalloc
	leaq Person$$, %rdx
	movq %rdx, 0(%rax)
	movq 0(%rax), %rax
	call *8(%rax)
	movq %rax, %rdi
	call put
	ret
Person$testA:
	movq $401, %rax
	movq %rax, %rdi
	call put
	movq $1, %rax
	ret
Person$testB:
	movq $2020, %rax
	movq %rax, %rdi
	call put
	movq $1, %rax
	ret
	.data
Person$$: .quad 0
	.quad Person$testA
	.quad Person$testB
