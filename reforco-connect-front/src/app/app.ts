import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

// Shell da aplicação: barra lateral fixa + área de conteúdo das rotas.
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  // Eager também no shell: se o pai for OnPush (padrão do Angular 22),
  // a verificação não desce até as páginas.
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class App {}
