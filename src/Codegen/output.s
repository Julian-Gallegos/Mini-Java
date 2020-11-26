	.text
	.globl asm_main
asm_main:
	movq $3 %rax
	pushq %rax
	movq $2 %rax
	pushq %rax
	movq $5 %rax
	popq %rdx
	imulq %rdx, %rax
	popq %rdx
	addq %rdx, %rax
	movq %rax %rdi
	call put
