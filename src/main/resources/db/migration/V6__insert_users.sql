INSERT INTO public.usuario (nome, email, login, senha, tipo_usuario, ativo)
VALUES
    ('Maria Oliveira', 'maria.oliveira@email.com', 'mariaoliveira', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'CLIENTE', true),
    ('Carlos Santos', 'carlos.santos@email.com', 'carlossantos', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'DONO_DE_RESTAURANTE', true),
    ('Ana Souza', 'ana.souza@email.com', 'anasouza', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'CLIENTE', true),
    ('Pedro Rocha', 'pedro.rocha@email.com', 'pedrorocha', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'DONO_DE_RESTAURANTE', true),
    ('Fernanda Lima', 'fernanda.lima@email.com', 'fernandalima', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'CLIENTE', true),
    ('Ricardo Costa', 'ricardo.costa@email.com', 'ricardocosta', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'DONO_DE_RESTAURANTE', true),
    ('Juliana Martins', 'juliana.martins@email.com', 'julianamartins', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'CLIENTE', true),
    ('Lucas Pereira', 'lucas.pereira@email.com', 'lucaspereira', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'DONO_DE_RESTAURANTE', true),
    ('Camila Rodrigues', 'camila.rodrigues@email.com', 'camilarodrigues', '$2a$10$JSB4FpsZ74GuzJYTNHRbuu8zRR1QSO8mQZ8NLt2aAFIYJvdb/9Vlu', 'CLIENTE', true)
    ON CONFLICT (login) DO NOTHING;
