package estacio.mestredosmago.prototipo.controller;

import estacio.mestredosmago.prototipo.parte.ParteRepository;
import estacio.mestredosmago.prototipo.parte.Parte;
import estacio.mestredosmago.prototipo.parte.dtos.DadosCadastroParte;
import estacio.mestredosmago.prototipo.parte.dtos.DadosListagemParte;
import estacio.mestredosmago.prototipo.processo.ProcessoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("partes")
public class ParteController {
    @Autowired
    private ParteRepository repository;
    @Autowired
    private ProcessoRepository processoRepository;


    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody DadosCadastroParte dados, UriComponentsBuilder uriBuilder) {
        var processo = processoRepository.getReferenceById(dados.numProcesso());

        var parte = new Parte(dados, processo);
        repository.save(parte);

        var uri = uriBuilder.path("/partes/{id}").buildAndExpand(parte.getIdParte()).toUri();
        return ResponseEntity.ok(new DadosListagemParte(parte));
    }
    @GetMapping
    public ResponseEntity getProcessos(@PageableDefault(size = 10, sort = {"statusProcesso"}) Pageable paginacao) {

        var page = repository.findAll();
        var dados = repository.findAll().stream().map(p -> new DadosListagemParte(p));
        //criar paginacao



        return ResponseEntity.ok(dados);
    }


}
