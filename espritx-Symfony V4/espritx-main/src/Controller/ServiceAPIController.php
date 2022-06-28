<?php

namespace App\Controller;

use App\Entity\Group;
use App\Entity\Service;
use App\Form\Service1Type;
use App\Form\ServiceType;
use App\Repository\ServiceRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\EntityManager;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\Serializer\SerializerInterface;

/**
 * @Route("/api/service")
 */
class ServiceAPIController extends AbstractApiController
{
    /**
     * @Route("/show", name="app_service_api_index", methods={"GET"})
     */
    public function getServices(ServiceRepository $serviceRepository,SerializerInterface $serializer)
    {
        $services = $serviceRepository->findAll();
        return $this->json($services);
    }

/*
    public function edit(Request $request, Service $service, EntityManagerInterface $entityManager,SerializerInterface $serializer): Response
    {
        $service1=$request->getContent();
        $serializer->deserialize($service1,Service::class,'json',['object_to_populate' => $service]);
        $entityManager->persist($service);
        $entityManager->flush();
        return new Response("Service updated successfully!");
    }


    public function delete(Request $request, Service $service, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$service->getId(), $request->request->get('_token'))) {
            $entityManager->remove($service);
            $entityManager->flush();
        }
        return new Response("Service deleted successfully!");
    }
*/
    /**
     * @Route("/add/", name="add_api_service", methods={"POST"})
     */
    public function addService(Request $request,ServiceRepository $repository, EntityManagerInterface $entityManager): JsonResponse
    {
        $data = json_decode($request->getContent(), true);

        $Name = $data['Name'];
        $Responsible = $entityManager->getRepository(Group::class)->findOneBy(['display_name'=>$data['Responsible']]);
        $Recipients = $data['Recipient'];

        if (empty($Name) || empty($Responsible) || empty($Recipients)) {
            throw new NotFoundHttpException('Expecting mandatory parameters!');
        }

        $repository->saveService($Name, $Responsible, $Recipients);

        return new JsonResponse(['status' => 'Service created!'], Response::HTTP_CREATED);
    }

    /**
     * @Route("/{id}/edit", name="update_api_service", methods={"PATCH"})
     */
    public function updateService($id, Request $request,ServiceRepository $serviceRepository, EntityManagerInterface $entityManager): JsonResponse
    {
        $S = $serviceRepository->findOneBy(['id' => $id]);
        $data = json_decode($request->getContent(), true);

        empty($data['Name']) ? true : $S->setName($data['Name']);
        empty($data['Responsible']) ? true : $S->setResponsible($entityManager->getRepository(Group::class)->findOneBy(['display_name'=>$data['Responsible']]));
        $Recipients = $data['Recipient'];

        $S->resetRecipient();
        for ($i=0;$i<count($Recipients);$i++) {
            $Recipient = $entityManager->getRepository(Group::class)->findOneBy(['display_name'=>$Recipients[$i]]);
            $S->addRecipient($Recipient);
        }

        $updatedRequest = $serviceRepository->updateService($S);

        return new JsonResponse(['status' => 'Service updated!'], Response::HTTP_OK);
    }

    /**
     * @Route("/{id}/delete", name="delete_api_service", methods={"DELETE"})
     */
    public function delete($id,ServiceRepository $serviceRepository): JsonResponse
    {
        $S = $serviceRepository->findOneBy(['id' => $id]);

        $serviceRepository->removeService($S);

        return new JsonResponse(['status' => 'Service deleted'], Response::HTTP_NO_CONTENT);
    }


}
