package ta

//import org.grails.datastore.mapping.query.Query​

class Student {
    String name;
    String login;
    double average;
    List criteriaAndEvaluations
    static hasMany = [criteriaAndEvaluations:EvaluationsByCriterion]

    static constraints = {
        name blank : false
        login unique : true, blank:false;
    }

    static mapping = {
        sort "login"
        sort login: "asc"
    }

    public Student(String name, String login){
        this.name = name;
        this.login = login;
        this.criteriaAndEvaluations = [];
    }

    public void calcMedia() {
        int qtdEvaluations = 0
        double tempMedia = 0
        List<Evaluation> evaluationsInCriterion
        for (int i = 0; i < this.criteriaAndEvaluations.size(); i++) {
            evaluationsInCriterion = this.criteriaAndEvaluations[i].getEvaluations()
            for (int j = 0; j < evaluationsInCriterion.size(); j++) {
                String eval = evaluationsInCriterion.get(j).value
                (qtdEvaluations, tempMedia) = EvaluationsByCriterion.calculateValueFromEvaluation(eval, qtdEvaluations, tempMedia)
            }
        }
        if (qtdEvaluations > 0) {
            tempMedia /= qtdEvaluations
            this.average = tempMedia
        } else {
            this.average = 0
        }
    }

    /*public void addEvaluation(Evaluation evaluationInstance){
        for(int i = 0; i< this.criteriaAndEvaluations.size(); i++){
            if(this.criteriaAndEvaluations.get(i).getCriterion().getDescription().equals(evaluationInstance.criterion.description)){
                this.criteriaAndEvaluations.get(i).addEvaluation(evaluationInstance);
            }
        }
    }*/

    public void addEvaluation(Evaluation evaluationInstance){
        if(this.findEvaluationByCriterion(evaluationInstance.getCriterion().getDescription()) != null) {
            for (int i = 0; i < this.criteriaAndEvaluations.size(); i++) {
                if (this.criteriaAndEvaluations[i].getCriterion().getDescription().equals(evaluationInstance.criterion.description)) {
                    this.criteriaAndEvaluations[i].addEvaluation(evaluationInstance)
                }
            }
        }else {
            EvaluationsByCriterion newEvByCrit = new EvaluationsByCriterion(evaluationInstance.criterion)
            newEvByCrit.addEvaluation(evaluationInstance)
            newEvByCrit.save(flush: true)
            this.addToCriteriaAndEvaluations(newEvByCrit)
        }
        this.calcMedia()

        /*
        Criterion criterionCreated = Criterion.findByDescription(criterionName)

        List<Evaluation> evaluationWithCriterion = Evaluation.findAllByCriterion(criterionCreated)
        Evaluation finalEvaluation
        for(int i = 0; i < evaluationWithCriterion.size(); i++){
            if(evaluationWithCriterion.get(i).getOrigin().equals(evaluationOrigin)){
                finalEvaluation = evaluationWithCriterion.get(i)
            }
        }

        def evaluationsByCriterionController = new EvaluationsByCriterionController()
        evaluationsByCriterionController.params << [criterion : criterionCreated]
        EvaluationsByCriterion evaluationsByCriterionCreated = evaluationsByCriterionController.createAndSaveEvaluationsByCriterion()
        evaluationsByCriterionCreated.addEvaluation(*//*evaluationInstance*//*finalEvaluation)

        this.criterionsAndEvaluations.add(evaluationsByCriterionCreated)

        evaluationsByCriterionController.response.reset()
        */
    }


    public void deleteEvaluation(Evaluation evaluationInstance){
        for(int i = 0; i< this.criteriaAndEvaluations.size(); i++){
            if(this.criteriaAndEvaluations[i].getCriterion().getDescription().equals(evaluationInstance.criterion.description)){
                this.criteriaAndEvaluations[i].deleteEvaluation(evaluationInstance);
            }
        }
    }

    public EvaluationsByCriterion findEvaluationByCriterion(String criterionName){
        for(int i =0; i<this.criteriaAndEvaluations.size(); i++){
            if(this.criteriaAndEvaluations[i].getCriterion().getDescription().equals(criterionName)){
                return this.criteriaAndEvaluations[i];
            }
        }
        return null
    }

    public void addEvaluationsByCriterion(EvaluationsByCriterion evCriterion){
        if(!this.findEvaluationByCriterion(evCriterion.getCriterion().getDescription())){
            this.addToCriteriaAndEvaluations(evCriterion);
        }
    }

    public boolean evaluationExist(Evaluation evaluationInstance){
        for(int i = 0; i<this.criteriaAndEvaluations.size(); i++){
            if(this.criteriaAndEvaluations[i].getCriterion().getDescription().equals(evaluationInstance.getCriterion().getDescription())){
                List<Evaluation> evaluationsForThisCriterion = this.criteriaAndEvaluations[i].evaluations;
                for(int j=0; j<evaluationsForThisCriterion.size();j++){
                    //if(evaluationsForThisCriterion.compatibleTo(evaluationInstance)){
                    //    return true
                    //}
                }
            }
        }
        return false
    }

    /*private boolean criterionExists(String criterionDescription){
        for(int i=0;i<this.criteriaAndEvaluations.size();i++){
            if(this.criteriaAndEvaluations.get(i).criterion.description.equals(criterionDescription))
        }
    }*/
}