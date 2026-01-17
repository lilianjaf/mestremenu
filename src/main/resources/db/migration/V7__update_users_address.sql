UPDATE public.usuario
SET logradouro = 'Rua dos Testes',
    numero = '123',
    complemento = 'Apto 1',
    bairro = 'Centro',
    cep = '12345-678',
    cidade = 'Cidade de Teste',
    uf = 'TS'
WHERE logradouro IS NULL;
