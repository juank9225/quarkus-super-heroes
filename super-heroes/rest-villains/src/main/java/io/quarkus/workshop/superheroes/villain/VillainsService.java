package io.quarkus.workshop.superheroes.villain;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class VillainsService {

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Villain> findAllVillains(){
        return Villain.listAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Villain findVillainById(Long id){
        return Villain.findById(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Villain findRandomVillain(){
        Villain randomVillain = null;
        while (randomVillain == null){
            randomVillain = Villain.findRandom();
        }
        return randomVillain;
    }

    public Villain persistVillain(@Valid Villain villain){
        villain.persist();
        return villain;
    }

    public Villain updateVilllain(@Valid Villain villain){
        Villain entity = Villain.findById(villain.id);
        entity.name = villain.name;
        entity.otherName = villain.otherName;
        entity.level = villain.level;
        entity.picture = villain.picture;
        entity.powers = villain.powers;
        return entity;
    }

    public void deleteVillain(Long id){
        Villain villain = Villain.findById(id);
        villain.delete();
    }
}
