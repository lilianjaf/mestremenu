INSERT INTO public.usuario (nome, email, login, senha, tipo_usuario, ativo)
VALUES ('Administrador', 'admin@mestremenu.com.br', 'admin', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'DONO_RESTAURANTE', true)
    ON CONFLICT (login) DO NOTHING;
