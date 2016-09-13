# Pesquisa aplicada à determinação de trajetos em viagens

## Objectivo
Determinar o melhor percurso a realizar de automóvel, numa viagem de grande distância.

## Descrição
O Sr. X gosta de viajar de automóvel com a sua família. Ao planear as suas férias, o Sr. X gosta de saber com o que contar.
Assim, pretende uma aplicação que lhe forneça um itinerário possível, tendo em conta diversos factores, tais como:
- o local de destino pode estar a uma grande distância, só percorrida ao longo de vários dias;
- para além da distância associada, há que ter em conta as portagens em determinadas estradas;
- é necessário determinar onde abastecer o carro de combustível, quando necessário;
- não sendo possível conduzir mais do que H horas diárias, torna-se necessário antecipar locais de pernoita, com custos associados;
- pode haver interesse em efetuar paragens em locais específicos ao longo da viagem.

Pretende-se portanto obter o melhor itinerário possível (com menor custo).
Devem ser utilizados métodos de pesquisa informada (nomeadamente A*) para descobrir o melhor itinerário, de acordo com os critérios escolhidos pelo utilizador. 
A solução deve ser visualizada de forma amigável, incluindo o percurso a realizar e as estadias realizadas.
É valorizada uma modelização realista do problema.

## Metodologias
Simulação, Pesquisa Sistemática/Informada, Algoritmo A*, Heurísticas

## Ferramentas
Linguagem C++, Java ou Prolog

## Bibliografia
Apontamentos das Aulas; "Artificial Intelligence: A Modern Approach"

## Compilação

```
$ mkdir -p bin
$ cp -r res bin/
$ javac -d bin/ -cp lib/gs-core-1.3.jar:lib/gs-ui-1.3.jar:. src/agents/*.java src/algorithms/*.java src/graph/*.java src/gui/*.java src/utils/*.java
```

## Execução

```
$ cd bin
$ java -cp ../lib/gs-core-1.3.jar:../lib/gs-ui-1.3.jar:. gui.TripPlanner
```
