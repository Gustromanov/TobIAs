document.addEventListener('DOMContentLoaded', function() {
    const cpfInputs = document.querySelectorAll('[data-cpf-mask]');

    const formatCpf = function(value) {
        const digits = value.replace(/\D/g, '').slice(0, 11);

        if (digits.length <= 3) {
            return digits;
        }

        if (digits.length <= 6) {
            return digits.replace(/(\d{3})(\d+)/, '$1.$2');
        }

        if (digits.length <= 9) {
            return digits.replace(/(\d{3})(\d{3})(\d+)/, '$1.$2.$3');
        }

        return digits.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
    };

    const calculateCpfDigit = function(cpf, length) {
        let sum = 0;

        for (let i = 0; i < length; i++) {
            sum += Number(cpf.charAt(i)) * (length + 1 - i);
        }

        const remainder = (sum * 10) % 11;
        return remainder === 10 ? 0 : remainder;
    };

    const isValidCpf = function(cpf) {
        const digits = cpf.replace(/\D/g, '');

        if (digits.length !== 11 || /^(\d)\1{10}$/.test(digits)) {
            return false;
        }

        return calculateCpfDigit(digits, 9) === Number(digits.charAt(9))
            && calculateCpfDigit(digits, 10) === Number(digits.charAt(10));
    };

    const updateCpfValidity = function(input) {
        const digits = input.value.replace(/\D/g, '');

        if (digits.length === 0 || digits.length < 11) {
            input.setCustomValidity('');
            return;
        }

        input.setCustomValidity(isValidCpf(input.value) ? '' : 'Informe um CPF válido.');
    };

    cpfInputs.forEach(function(input) {
        input.value = formatCpf(input.value);
        updateCpfValidity(input);

        input.addEventListener('input', function() {
            input.value = formatCpf(input.value);
            updateCpfValidity(input);
        });

        input.addEventListener('paste', function(event) {
            event.preventDefault();

            const pastedText = event.clipboardData.getData('text');
            input.value = formatCpf(pastedText);
            updateCpfValidity(input);
        });

        if (input.form) {
            input.form.addEventListener('submit', function() {
                updateCpfValidity(input);
            });
        }
    });
});
